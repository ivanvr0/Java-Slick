package org.newdawn.slick.particles;

/**
 * An emitter is responsible for producing the particles and controlling them during
 * their life. An implementation of this interface can be considered a particle
 * effect.
 *
 * @author kevin
 */
public interface ParticleEmitter {
	/**
	 * Update the emitter, produce any particles required by requesting
	 * them from the particle system provided.
	 * 
	 * @param system The particle system used to create particles
	 * @param delta The amount of time in milliseconds since last emitter update
	 */
	public void update(ParticleSystem system, int delta);
	
	/**
	 * Update a single particle that this emitter produced
	 * 
	 * @param particle The particle to be updated
	 * @param delta The amount of time in millisecond since last particle update
	 */
	public void updateParticle(Particle particle, int delta);
}
