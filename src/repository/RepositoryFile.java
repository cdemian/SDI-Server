package repository;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RepositoryFile<K, V> implements IRepository<K, V> {
	private String filePath;
	Map<K, V> objects = new HashMap<K, V>();

	public RepositoryFile(String filePath) {
		this.filePath = filePath;
		persistantReadObjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repository.IRepository#getStudents()
	 */
	@Override
	public ArrayList<V> getObjects() {
		ArrayList<V> l = new ArrayList<V>();
		for (V v: objects.values()){
			l.add(v);
		}
		return l;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repository.IRepository#getObject(K)
	 */
	@Override
	public V getObject(K key) {
		return objects.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repository.IRepository#storeObject(K, V)
	 */
	@Override
	public void storeObject(K key, V value) {
		objects.put(key, value);
		persistantSaveObjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repository.IRepository#deleteObject(K)
	 */
	@Override
	public void deleteObject(K key) {
		objects.remove(key);
		persistantSaveObjects();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see repository.IRepository#updateObject(K, V)
	 */
	@Override
	public void updateObject(K key, V value) {
		objects.put(key, value);
		persistantSaveObjects();
	}

	private void persistantSaveObjects() {
		try {
			FileOutputStream fos = new FileOutputStream(filePath);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(objects);
			oos.close();
		} catch (Exception ex) {
			System.out.println("Error at saving objects: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	private void persistantReadObjects() {
		try {
			FileInputStream fis = new FileInputStream(filePath);
			ObjectInputStream ois = new ObjectInputStream(fis);
			objects = (HashMap<K, V>) ois.readObject();
			ois.close();
		} catch (FileNotFoundException ex) {
			return;
		} catch (Exception ex) {
			System.out.println("Error at loading objects: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
