/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.physics;

import com.badlogic.gdx.math.*;
import java.util.Arrays;

/**
 *
 * @author lachlan
 */
public class PhysicsEngine {

    public static float SPRING = 100f;

    private final int bsize, psize;
    private final Buffer current;

    private int nextBodyId, nextParticleId;

    private final Rectangle worldSize = new Rectangle();

    public PhysicsEngine() {
        this.bsize = 32;
        this.psize = 4 * 1024;

        this.current = new Buffer(bsize, psize);

        Arrays.fill(current.pbodyId, -1);
    }

    public int addBody(BodyMesh mesh, float x, float y, boolean fixed) {
        int bodyId = nextBodyId;
        nextBodyId++;

        current.bpos[bodyId * 2] = x;
        current.bpos[bodyId * 2 + 1] = y;
        current.bfixed[bodyId] = fixed;

        int count = 0;
        float comX = 0, comY = 0;
        for (int px = 0; px < mesh.getWidth(); px++) {
            for (int py = 0; py < mesh.getHeight(); py++) {
                if (mesh.getAt(px, py)) {
                    int pid = nextParticleId;
                    nextParticleId++;
                    count++;

                    current.pbodyId[pid] = bodyId;
                    current.pmass[pid] = 0.25f;

                    current.prel[pid * 2] = px * BodyMesh.PARTICLE_RADIUS;
                    current.prel[pid * 2 + 1] = py * BodyMesh.PARTICLE_RADIUS;

                    comX += current.prel[pid * 2];
                    comY += current.prel[pid * 2 + 1];
                }
            }
        }

        comX = comX / count;
        comY = comY / count;

        current.bpos[bodyId * 2] += comX;
        current.bpos[bodyId * 2 + 1] += comY;

        for (int i = 0; i < current.psize; i++) {
            if (current.pbodyId[i] != bodyId)
                continue;
            current.prel[i * 2] -= comX;
            current.prel[i * 2 + 1] -= comY;
        }
        System.out.println(nextParticleId + " particles");
        return bodyId;
    }

    public void removeBody(int bodyId) {
    }

    public Vector2 getPositionOf(int bodyId) {
        return new Vector2(current.bpos[bodyId * 2], current.bpos[bodyId * 2 + 1]);
    }

    public float getRotationOf(int bodyId) {
        return current.borient[bodyId];
    }

    public void update(float dt) {
        initialiseParticles();
        collideParticles(dt);
        computeRigidBodies(dt);

        //swapBuffers();
    }

    private void initialiseParticles() {
        Arrays.fill(current.bmass, 0);

        for (int i = 0; i < current.psize; i++) {
            int bid = current.pbodyId[i];
            if (bid == -1)
                continue;

            float relX = current.prel[i * 2];
            float relY = current.prel[i * 2 + 1];
            float cs = (float) MathUtils.cos(current.borient[bid]);
            float sn = (float) MathUtils.sin(current.borient[bid]);

            float rotX = relX * cs - relY * sn;
            float rotY = relX * sn + relY * cs;

            current.protRel[i * 2] = rotX;
            current.protRel[i * 2 + 1] = rotY;

            current.ppos[i * 2] = rotX + current.bpos[bid * 2];
            current.ppos[i * 2 + 1] = rotY + current.bpos[bid * 2 + 1];

            current.bmass[bid] += current.pmass[i];
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

                float diameter = BodyMesh.PARTICLE_RADIUS * 2;

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
        worldSize.x = 0;
        worldSize.y = 0;
        worldSize.width = 0;
        worldSize.height = 0;

        for (int i = 0; i < current.psize; i++) {
            int bid = current.pbodyId[i];
            if (bid == -1)
                continue;

            float rotX = current.protRel[i * 2];
            float rotY = current.protRel[i * 2 + 1];
            float pimpx = current.pimp[i * 2];
            float pimpy = current.pimp[i * 2 + 1];

            current.bimp[bid * 2] += pimpx;
            current.bimp[bid * 2 + 1] += pimpy;
            current.baimp[bid] += cross(rotX, rotY, pimpx, pimpy);

            current.pimp[i * 2] = 0;
            current.pimp[i * 2 + 1] = 0;
        }

        for (int i = 0; i < current.bsize; i++) {
            current.bimp[i * 2 + 1] = current.bimp[i * 2 + 1] - 9.81f * dt;

            if (!current.bfixed[i]) {
                float aAccel;
                if (current.bmass[i] == 0)
                    aAccel = 0;
                else
                    aAccel = current.baimp[i] / (0.5f * current.bmass[i] * 2 * 2);

                current.bvel[i * 2] = current.bvel[i * 2] + current.bimp[i * 2];
                current.bvel[i * 2 + 1] = current.bvel[i * 2 + 1] + current.bimp[i * 2 + 1];

                current.bpos[i * 2] = current.bpos[i * 2] + current.bvel[i * 2] * dt;
                current.bpos[i * 2 + 1] = current.bpos[i * 2 + 1] + current.bvel[i * 2 + 1] * dt;

                current.bavel[i] = current.bavel[i] + aAccel;
                current.borient[i] = current.borient[i] + current.bavel[i];
            }

            current.bimp[i * 2] = 0;
            current.bimp[i * 2 + 1] = 0;
            current.baimp[i] = 0;
        }
    }

    private float cross(float _1x, float _1y, float _2x, float _2y) {
        return (_1x * _2y) - (_1y * _2x);
    }

    public Buffer getCurrentBuffer() {
        return current;
    }

    public static class Buffer {

        private final int bsize, psize;

        public final float[] bpos;
        public final float[] bvel;
        public final float[] bimp;
        public final float[] borient;
        public final float[] bavel;
        public final float[] baimp;
        public final float[] bmass;
        public final boolean[] bfixed;

        public final int[] pbodyId;
        public final float[] ppos;
        public final float[] prel;
        public final float[] protRel;
        public final float[] pvel;
        public final float[] pimp;
        public final float[] pmass;

        Buffer(int bsize, int psize) {
            this.bsize = bsize;
            this.psize = psize;

            bpos = new float[bsize * 2];
            bvel = new float[bsize * 2];
            bimp = new float[bsize * 2];
            borient = new float[bsize];
            bavel = new float[bsize];
            baimp = new float[bsize];
            bmass = new float[bsize];
            bfixed = new boolean[bsize];

            pbodyId = new int[psize];
            ppos = new float[psize * 2];
            prel = new float[psize * 2];
            protRel = new float[psize * 2];
            pvel = new float[psize * 2];
            pimp = new float[psize * 2];
            pmass = new float[psize];
        }

        public int getBSize() {
            return bsize;
        }

        public int getPSize() {
            return psize;
        }
    }
}
