package com.aptech.infrastructure;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.datatype.Artwork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aptech.domain.MediaFile;
import com.aptech.domain.MetaData;
import com.aptech.util.StringUtil;

public final class JaudiotaggerParser {
	private final static Logger logger = LoggerFactory.getLogger(JaudiotaggerParser.class);

	private static final Pattern TRACK_NUMBER_PATTERN = Pattern.compile("(\\d+)/\\d+");
	private static final Pattern YEAR_NUMBER_PATTERN = Pattern.compile("(\\d{4}).*");

	public static MetaData getRawMetaData(File file) {

		MetaData metaData = new MetaData();

		try {
			AudioFile audioFile = AudioFileIO.read(file);
			Tag tag = audioFile.getTag();
			if (tag != null) {
				metaData.setAlbumArtist(getTagField(tag, FieldKey.ALBUM_ARTIST));
				metaData.setAlbumName(getTagField(tag, FieldKey.ALBUM));
				metaData.setArtist(getTagField(tag, FieldKey.ARTIST));
				metaData.setComposer(getTagField(tag, FieldKey.COMPOSER));
				metaData.setProducer(getTagField(tag, FieldKey.PRODUCER));
				metaData.setDiscNumber(parseInteger(getTagField(tag, FieldKey.DISC_NO)));
				metaData.setGenre(getTagField(tag, FieldKey.GENRE));
				metaData.setLyric(getTagField(tag, FieldKey.LYRICS));
				metaData.setMusicBrainzRecordingId(getTagField(tag, FieldKey.MUSICBRAINZ_TRACK_ID));
				metaData.setMusicBrainzReleaseId(getTagField(tag, FieldKey.MUSICBRAINZ_RELEASEID));
				metaData.setTitle(getTagField(tag, FieldKey.TITLE));
				metaData.setTrackNumber(parseIntegerPattern(getTagField(tag, FieldKey.TRACK), TRACK_NUMBER_PATTERN));
				metaData.setYear(parseIntegerPattern(getTagField(tag, FieldKey.YEAR), YEAR_NUMBER_PATTERN));

				if (StringUtil.isBlank(metaData.getArtist())) {
					metaData.setArtist(metaData.getAlbumArtist());
				}
				if (StringUtil.isBlank(metaData.getAlbumArtist())) {
					metaData.setAlbumArtist(metaData.getArtist());
				}
			}

			AudioHeader audioHeader = audioFile.getAudioHeader();
			if (audioHeader != null) {
				metaData.setVariableBitRate(audioHeader.isVariableBitRate());
				metaData.setBitRate((int) audioHeader.getBitRateAsNumber());
				metaData.setDurationSeconds(audioHeader.getTrackLength());
			}

		} catch (Throwable x) {
			logger.error("Error when parsing tags in " + file, x);
		}

		return metaData;
	}

	private static String getTagField(Tag tag, FieldKey fieldKey) {
		try {
			return (tag.getFirst(fieldKey));
		} catch (Exception x) {
			// Ignored.
			return null;
		}
	}

	public static Artwork getArtwork(MediaFile file) {
		AudioFile audioFile;
		try {
			audioFile = AudioFileIO.read(file.getFile());
		} catch (Throwable e) {
			logger.warn("Failed to find cover art tag in " + file, e);
			return null;
		}
		Tag tag = audioFile.getTag();
		return tag == null ? null : tag.getFirstArtwork();
	}

	private static Integer parseIntegerPattern(String str, Pattern pattern) {
		if (str == null) {
			return null;
		}

		Integer result = null;

		try {
			result = Integer.valueOf(str);
		} catch (NumberFormatException x) {
			if (pattern == null) {
				return null;
			}
			Matcher matcher = pattern.matcher(str);
			if (matcher.matches()) {
				try {
					result = Integer.valueOf(matcher.group(1));
				} catch (NumberFormatException e) {
					return null;
				}
			}
		}

		if (Integer.valueOf(0).equals(result)) {
			return null;
		}
		return result;
	}

	private static Integer parseInteger(String s) {
		return parseIntegerPattern(StringUtil.trimOrNull(s), null);
	}
}
