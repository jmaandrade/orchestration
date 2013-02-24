package pt.jma.orchestration.test;


public class HelloWorld {

	String name = "";
	String sayHi = "";

	public String execute() {

		this.sayHi = String.format("Hi %s", this.name);
		return "ok";
	}

	public String getSayHi() {
		return sayHi;
	}

	public void setName(String name) {
		this.name = name;
	}
}
