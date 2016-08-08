package ch.zhaw.psit4.martin.language.typefactory;

import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

import ch.zhaw.psit4.martin.api.types.BaseTypeInstanciationException;
import ch.zhaw.psit4.martin.api.types.MLocation;
import ch.zhaw.psit4.martin.boot.MartinBoot;
import ch.zhaw.psit4.martin.timing.TimingInfoLogger;
import ch.zhaw.psit4.martin.timing.TimingInfoLoggerFactory;

public class MLocationFactory {
	private static final String GOOGLE_GEOLOCATION_API_KEY = "AIzaSyAqUfeSyNLB7YTslza6EqAZ9cHjSECg14U";

	private static final Log LOG = LogFactory.getLog(MartinBoot.class);
	private static final TimingInfoLogger TIMING_LOG = TimingInfoLoggerFactory.getInstance();

	public static MLocation fromString(String rawInput) throws BaseTypeInstanciationException {
		TIMING_LOG.logStart(GeocodingApi.class.getSimpleName());
		MLocation martinLocation = new MLocation(rawInput);

		GeocodingResult[] results = getGeolocationFromGoogle(rawInput);

		if (results.length > 0) {
			martinLocation.setFormattedAddress(Optional.ofNullable(results[0].formattedAddress));
			martinLocation.setLatitude(Optional.ofNullable(results[0].geometry.location.lat));
			martinLocation.setLongitude(Optional.ofNullable(results[0].geometry.location.lng));
		}
		
		TIMING_LOG.logEnd(GeocodingApi.class.getSimpleName());
		return martinLocation;
	}

	private static GeocodingResult[] getGeolocationFromGoogle(String rawLocation) {
		GeoApiContext context = new GeoApiContext().setApiKey(GOOGLE_GEOLOCATION_API_KEY);
		try {
			return GeocodingApi.geocode(context, rawLocation).await();
		} catch (Exception e) {
			LOG.error(e);
		}

		return new GeocodingResult[0];
	}
}
