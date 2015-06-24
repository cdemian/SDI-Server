package repository;

import java.util.ArrayList;

public interface IRepository<K, V> {

	/**
	 * Get all the objects from this repository
	 * @return a list of objects stored in this repository
	 */
	public abstract ArrayList<V> getObjects();

	/**
	 * Get a particular object from this repository
	 * @param key the key or id of the object whit which it is associated
	 * @return the object which was stored with the given id, or null if the key is not present in the repository
	 */
	public abstract V getObject(K key);

	/**
	 * Store an object in the repository. If an object is already in the repository with the same key, the old object will be lost!
	 * @param key the key which will be associated with the object
	 * @param value the object to be stored
	 */
	public abstract void storeObject(K key, V value);

	/**
	 * Delete a stored object which was stored. If the key given is not associated with any key, the function does nothing.
	 * @param key the associated key to the object
	 */
	public abstract void deleteObject(K key);

	/**
	 * Update an existing object in the repository. If the key is not associated with any object, the function will proceed as adding a new object to the repository
	 * @param key 
	 * @param value
	 */
	public abstract void updateObject(K key, V value);

}