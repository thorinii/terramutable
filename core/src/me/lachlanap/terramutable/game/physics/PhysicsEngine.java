/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.physics;

import com.badlogic.gdx.math.Vector2;
import java.util.Arrays;

/**
 *
 * @author lachlan
 */
public class PhysicsEngine {

    public static float SPRING = 100f;

    private final int bsize, psize;
    private Buffer current, next;

    private int nextBodyId, nextParticleId;

    public PhysicsEngine() {
        this.bsize = 512;
        this.psize = 1 * 1024;

        this.current = new Buffer(bsize, psize);
        this.next = new Buffer(bsize, psize);

        Arrays.fill(current.pbodyId, -1);
    }

    public int addBody(BodyMesh mesh, float x, float y, boolean fixed) {
        int bodyId = nextBodyId;
        nextBodyId++;

        current.bpos[bodyId * 2] = x;
        current.bpos[bodyId * 2 + 1] = y;
        current.bfixed[bodyId] = fixed;

        next.bpos[bodyId * 2] = x;
        next.bpos[bodyId * 2 + 1] = y;
        next.bfixed[bodyId] = fixed;

        for (int px = 0; px < mesh.getWidth(); px++) {
            for (int py = 0; py < mesh.getHeight(); py++) {
                if (mesh.getAt(px, py)) {
                    int pid = nextParticleId;
                    nextParticleId++;

                    current.pbodyId[pid] = bodyId;
                    current.pmass[pid] = 0.25f;
                    current.prad[pid] = 0.05f;

                    current.prel[pid * 2] = px * current.prad[pid] * 2;
                    current.prel[pid * 2 + 1] = py * current.prad[pid] * 2;
                }
            }
        }

        System.out.println(nextParticleId + " particles");
        return bodyId;
    }

    public void removeBody(int bodyId) {
    }

    public Vector2 getPositionOf(int bodyId) {
        return new Vector2(current.bpos[bodyId * 2], current.bpos[bodyId * 2 + 1]);
    }

    public void update(float dt) {
        initialiseParticles();
        collideParticles(dt);
        computeRigidBodies(dt);

        //swapBuffers();
    }

    private void initialiseParticles() {
        for (int i = 0; i < current.psize; i++) {
            int bid = current.pbodyId[i];
            if (bid == -1)
                continue;

            current.ppos[i * 2] = current.prel[i * 2] + current.bpos[bid * 2];
            current.ppos[i * 2 + 1] = current.prel[i * 2 + 1] + current.bpos[bid * 2 + 1];
        }
    }

    private void collideParticles(float dt) {
        for (int i = 0; i < current.psize; i++) {
            if (current.pbodyId[i] == -1)
                continue;
            for (int j = 0; j < current.psize; j++) {
                if (i == j)
                    continue;
                if (current.pbodyId[j] == -1)
                    continue;

                // f = -k(d-|r|)(r/|r|)
                // f = -spring(diameter - abs(dist[j,i]))(dist[j,i]/abs(dist[j,i]))
                float distX = current.ppos[j * 2] - current.ppos[i * 2];
                float distY = current.ppos[j * 2 + 1] - current.ppos[i * 2 + 1];
                float dist = (float) Math.hypot(distX, distY);

                float diameter = current.prad[i] + current.prad[j];

                float forceX = -SPRING * (diameter - dist) * (distX / dist);
                float forceY = -SPRING * (diameter - dist) * (distY / dist);

                if (dist < diameter && current.pbodyId[i] != current.pbodyId[j]) {
                    current.pimp[i * 2] = forceX * dt;
                    current.pimp[i * 2 + 1] = forceY * dt;
                }
            }
        }
    }

    private void computeRigidBodies(float dt) {
        for (int i = 0; i < current.psize; i++) {
            int bid = current.pbodyId[i];
            if (bid == -1)
                continue;

            current.bimp[bid * 2] += current.pimp[i * 2];
            current.bimp[bid * 2 + 1] += current.pimp[i * 2 + 1];
        }

        for (int i = 0; i < current.bsize; i++) {
            current.bimp[i * 2 + 1] = current.bimp[i * 2 + 1] - 9.81f * dt;

            if (!current.bfixed[i]) {
                current.bvel[i * 2] = current.bvel[i * 2] + current.bimp[i * 2];
                current.bvel[i * 2 + 1] = current.bvel[i * 2 + 1] + current.bimp[i * 2 + 1];

                current.bpos[i * 2] = current.bpos[i * 2] + current.bvel[i * 2] * dt;
                current.bpos[i * 2 + 1] = current.bpos[i * 2 + 1] + current.bvel[i * 2 + 1] * dt;
            }

            current.bimp[i * 2] = 0;
            current.bimp[i * 2 + 1] = 0;
        }
    }

    private void swapBuffers() {
        Buffer tmp = current;
        current = next;
        next = tmp;
    }

    public Buffer getCurrentBuffer() {
        return current;
    }

    public static class Buffer {

        private final int bsize, psize;

        public final float[] bpos;
        public final float[] bvel;
        public final float[] bimp;
        public final boolean[] bfixed;

        public final int[] pbodyId;
        public final float[] ppos;
        public final float[] prel;
        public final float[] pvel;
        public final float[] pimp;
        public final float[] pmass;
        public final float[] prad;

        Buffer(int bsize, int psize) {
            this.bsize = bsize;
            this.psize = psize;

            bpos = new float[bsize * 2];
            bvel = new float[bsize * 2];
            bimp = new float[bsize * 2];
            bfixed = new boolean[bsize];

            pbodyId = new int[psize];
            ppos = new float[psize * 2];
            prel = new float[psize * 2];
            pvel = new float[psize * 2];
            pimp = new float[psize * 2];
            pmass = new float[psize];
            prad = new float[psize];
        }

        public int getBSize() {
            return bsize;
        }

        public int getPSize() {
            return psize;
        }
    }
}
