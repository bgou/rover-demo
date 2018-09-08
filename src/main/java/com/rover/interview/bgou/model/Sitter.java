package com.rover.interview.bgou.model;

import lombok.Builder;
import lombok.Data;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Log4j2
public class Sitter {
    private String name;
    private String phone;
    private String email;
    private String image;
    private float score;

    public static float calculateScore(String name) {
        // Sitter Score is 5 times the fraction of the English alphabet comprised by the distinct letters in
        // what we've recovered of the sitter's name.
        //
        // Say the sitter's name is 'Lauren B.', it has 7 distinct letters
        // fraction of alphabet, which is 26 characters
        // So the sitter score is 5 * (7 / 26) = 1.346153846

        Set<Character> set = new HashSet<>();
        if (!StringUtils.isEmpty(name)) {
            for (Character c : name.toCharArray()) {
                if (StringUtils.isAlpha(String.valueOf(c))) {
                    set.add(c);
                }
            }
        }
        return (float) (5.0 * (set.size() / 26.0));
    }
}
