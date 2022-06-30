/* 
 * This code is based on this PR: https://github.com/hibernate/hibernate-validator/pull/1199
 * All credits to Daniel Heid (dheid). I just made some adjustments.
 * In order to apply this on your situation, just create this class and call the method "isValidStringUUID". As its param, pass in your string UUID.
*/

public class UUIDValidator {
    enum LetterCase {

		/**
		 * Only lower case is valid
		 */
		LOWER_CASE,

		/**
		 * Only upper case is valid
		 */
		UPPER_CASE,

		/**
		 * Every letter case is valid
		 */
		INSENSITIVE

	}

    private static LetterCase letterCase = LetterCase.LOWER_CASE;
    
    private static final int[] GROUP_LENGTHS = { 8, 4, 4, 4, 12 };

	public static boolean isValidStringUUID(String value) {
		if ( value == null ) {
			return false;
		}
		int valueLength = value.length();
		if ( valueLength == 0 ) {
			return false;
		}
		else if ( valueLength != 36 ) {
			return false;
		}

		int groupIndex = 0;
		int groupLength = 0;
		int checksum = 0;
		for ( int charIndex = 0; charIndex < valueLength; charIndex++ ) {

			char ch = value.charAt( charIndex );

			if ( ch == '-' ) {
				groupIndex++;
				groupLength = 0;
			}
			else {

				groupLength++;
				if ( groupLength > GROUP_LENGTHS[groupIndex] ) {
					return false;
				}

				int numericValue = Character.digit( ch, 16 );
				if ( numericValue == -1 ) {
					// not a hex digit
					return false;
				}
				if ( numericValue > 9 && !hasCorrectLetterCase( ch ) ) {
					return false;
				}
				checksum += numericValue;
			}
		}
        // NIL UUID
		if ( checksum == 0 ) {
			return false;
		}
        return true;
	}
    private static boolean hasCorrectLetterCase(char ch) {
		if ( letterCase == null ) {
			return true;
		}
		if ( letterCase == LetterCase.LOWER_CASE && !Character.isLowerCase( ch ) ) {
			return false;
		}
		return letterCase != LetterCase.UPPER_CASE || Character.isUpperCase( ch );
	}
}
