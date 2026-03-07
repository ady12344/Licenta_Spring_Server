package com.licenta.server.models;

public enum ReviewSentiment {
    OVERWHELMINGLY_NEGATIVE,
    MOSTLY_NEGATIVE,
    MIXED,
    MOSTLY_POSITIVE,
    POSITIVE,
    OVERWHELMINGLY_POSITIVE;

    public static ReviewSentiment fromScore(long positive, long total) {
        if (total == 0) return null;
        double ratio = (double) positive / total * 100;

        if (ratio < 40)      return OVERWHELMINGLY_NEGATIVE;
        else if (ratio < 50) return MOSTLY_NEGATIVE;
        else if (ratio < 60) return MIXED;
        else if (ratio < 75) return MOSTLY_POSITIVE;
        else if (ratio < 90) return POSITIVE;
        else                 return OVERWHELMINGLY_POSITIVE;
    }
}
