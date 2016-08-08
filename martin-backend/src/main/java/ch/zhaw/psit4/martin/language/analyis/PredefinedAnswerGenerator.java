package ch.zhaw.psit4.martin.language.analyis;

import java.util.ArrayList;
import java.util.List;

import ch.zhaw.psit4.martin.api.language.parts.Sentence;
import ch.zhaw.psit4.martin.api.types.output.MOutput;
import ch.zhaw.psit4.martin.api.types.output.MOutputType;

public class PredefinedAnswerGenerator {
	/**
	 * Generates predefined answers, that can be used for static stentences.
	 */
	public static List<MOutput> generadePredefinedAnswer(Sentence sentence) {
		List<MOutput> predefinedAnswer = new ArrayList<>();
		
		if ("".equalsIgnoreCase(sentence.getText())) {
			predefinedAnswer.add(new MOutput(MOutputType.TEXT, "I can't hear you. Please speak louder."));
		}

		if ((sentence.getWords().contains("unit") && sentence.getWords().contains("tests"))
				|| sentence.getWords().contains("unittests")) {
			predefinedAnswer.add(new MOutput(MOutputType.HEADING, "Just be quiet!"));
			predefinedAnswer.add(new MOutput(MOutputType.TEXT, "I'm gonna getcha!"));
			predefinedAnswer.add(new MOutput(MOutputType.IMAGE, "http://tclhost.com/gEFAjgp.gif"));

		}

		if ("test".equalsIgnoreCase(sentence.getText())) {
			predefinedAnswer.add(new MOutput(MOutputType.HEADING, "Heading"));
			predefinedAnswer.add(new MOutput(MOutputType.TEXT,
					"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. \n\nAt vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. \n\nStet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet."));
			predefinedAnswer.add(new MOutput(MOutputType.ERROR,
					"Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua!"));
			predefinedAnswer.add(new MOutput(MOutputType.IMAGE,
					"http://www.aviatorcameragear.com/wp-content/uploads/2012/07/placeholder_2.jpg"));
		}

		if (sentence.getText().toLowerCase().startsWith("can you")) {
			predefinedAnswer.add(new MOutput(MOutputType.IMAGE, "http://tclhost.com/YXRMgbt.gif"));
		}
		
		return predefinedAnswer;
	}
}
