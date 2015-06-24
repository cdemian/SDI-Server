package model;

import java.io.Serializable;

public class Student implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String id;
	public String name;
	public String group;
	
	public String toString(){
		return id + ". " + name + " (" + group + ")";
	}
}