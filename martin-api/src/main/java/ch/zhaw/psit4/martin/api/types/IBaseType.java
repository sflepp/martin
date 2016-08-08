package ch.zhaw.psit4.martin.api.types;

import java.lang.reflect.Constructor;
import java.util.Optional;

import org.json.JSONObject;

import ch.zhaw.psit4.martin.api.language.parts.ISentence;

/**
 * Distributed generic Type for MArtIn objects.
 * 
 * Martin wraps Objects, that are passed on to plugins in this container for
 * easy handling.
 *
 * @version 0.0.1-SNAPSHOT
 */
public interface IBaseType {
	/**
	 * Serialize this Object into a string in Text format.
	 * 
	 * @return The serialized {@link String}.
	 */
	@Override
	public String toString();

	/**
	 * De-serialize this object from a {@link String} in Text format.
	 * 
	 * @param data
	 *            The string filed with data to deserialize.
	 */
	public static IBaseType fromString(EBaseType type, String data) throws BaseTypeInstanciationException{
		try {
			Constructor<? extends IBaseType> constructor = Class.forName(type.getValue())
					.asSubclass(IBaseType.class).getConstructor(String.class);
			return constructor.newInstance(data);
		} catch (Exception e) {
			throw new BaseTypeInstanciationException(e);
		}
	}

	/**
	 * Serialize this Object into a string in JSON format.
	 * 
	 * @return The serialized {@link String}.
	 */
	public String toJson();

	/**
	 * De-serialize this object from a {@link String} in JSON format.
	 * 
	 * @param json
	 *            The string filed with data to deserialize.
	 * @throws BaseTypeInstanciationException
	 */
	public static IBaseType fromJSON(String json) throws BaseTypeInstanciationException {
		try {
			JSONObject jsonObject = new JSONObject(json);
			Constructor<? extends IBaseType> constructor = Class.forName(jsonObject.getString("type"))
					.asSubclass(IBaseType.class).getConstructor(String.class);
			return constructor.newInstance(jsonObject.getString("data"));
		} catch (Exception e) {
			throw new BaseTypeInstanciationException(e);
		}
	}
	
	public Optional<ISentence> getParentSentence();

	public void setParentSentence(ISentence parentSentence);
}
