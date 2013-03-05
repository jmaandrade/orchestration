package pt.jma.orchestration.test;

public class HelloWorld2 {

	String language = "";
	String name = "";
	String sayHi = "";

	String mask = "";

	public String detectLanguage() {

		if (this.language.startsWith("pt"))
			return "local";
		else
			return "international";
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String execute() {

		this.sayHi = String.format(mask, this.name);

		return "ok";
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
	}

	public String getSayHi() {
		return sayHi;
	}

	public void setName(String name) {
		this.name = name;
	}
}
