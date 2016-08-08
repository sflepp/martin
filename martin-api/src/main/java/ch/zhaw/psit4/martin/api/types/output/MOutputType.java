package ch.zhaw.psit4.martin.api.types.output;

public enum MOutputType {

	TEXT("text"), IMAGE("image"), HEADING("heading"), ERROR("error"), TIMING_INFO("timing_info"), AUDIO("audio");

	private String name;

	private MOutputType(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
