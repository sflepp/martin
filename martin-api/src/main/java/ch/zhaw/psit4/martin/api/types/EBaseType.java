package ch.zhaw.psit4.martin.api.types;

import java.util.HashMap;
import java.util.Map;

public enum EBaseType {
	DURATION(MDuration.class.getName()), LOCATION(MLocation.class.getName()), MISC(
			MMisc.class.getName()), MONEY(MMoney.class.getName()), NUMBER(MNumber.class.getName()), ORDINAL(
					MOrdinal.class.getName()), ORGANIZATION(MOrganization.class.getName()), PERCENT(
							MPercent.class.getName()), PERSON(MPerson.class.getName()), SET(MSet.class.getName()), NOMINAL_MODIFIER(
									MNominalModifier.class.getName()), TIMESTAMP(MTimestamp.class.getName()), UNKNOWN(MUnknown.class.getName());

	private static final Map<String, String> NLP_TAGS;

	static {
		NLP_TAGS = new HashMap<>();
		NLP_TAGS.put(MDuration.class.getName(), "DURATION");
		NLP_TAGS.put(MLocation.class.getName(), "LOCATION");
		NLP_TAGS.put(MMisc.class.getName(), "MISC");
		NLP_TAGS.put(MMoney.class.getName(), "MONEY");
		NLP_TAGS.put(MNumber.class.getName(), "NUMBER");
		NLP_TAGS.put(MOrdinal.class.getName(), "ORDINAL");
		NLP_TAGS.put(MOrganization.class.getName(), "ORGANIZATION");
		NLP_TAGS.put(MPercent.class.getName(), "PERCENT");
		NLP_TAGS.put(MPerson.class.getName(), "PERSON");
		NLP_TAGS.put(MSet.class.getName(), "SET");
		NLP_TAGS.put(MNominalModifier.class.getName(), "NOMINAL_MODIFIER");
		NLP_TAGS.put(MTimestamp.class.getName(), "TIMESTAMP");
		NLP_TAGS.put(MUnknown.class.getName(), "O");
	}

	private String value;

	private EBaseType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static EBaseType fromClassName(String value) {
		for (EBaseType e : EBaseType.values()) {
			if (value.equals(e.value)) {
				return e;
			}
		}

		return null;
	}

	public static EBaseType fromNLPTag(String tag) {
		if(tag.equals("DATE") || tag.equals("TIME")){
			tag = "TIMESTAMP";
		}
		
		for (EBaseType e : EBaseType.values()) {
			if (tag.equals(e.getNerTag())) {
				return e;
			}
		}
		return null;
	}

	public String getNerTag() {
		return NLP_TAGS.get(value);
	}
}
