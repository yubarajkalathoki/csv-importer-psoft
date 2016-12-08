package com.yubaraj.csv.importer.psoft.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic util.
 * 
 * @author Yuba Raj Kalathoki
 */
public class GenericUtil {
    /**
     * Returns the given size of list.
     * 
     * @param list
     *            the list of object to be chopped.
     * @param l
     *            the value to chuped the lease
     * @author Yuba Raj Kalathoki
     */
    public static <T> List<List<T>> chopped(List<T> list, final int l) {
	List<List<T>> parts = new ArrayList<List<T>>();
	final int N = list.size();
	for (int i = 0; i < N; i += l) {
	    parts.add(new ArrayList<T>(list.subList(i, Math.min(N, i + l))));
	}
	return parts;
    }
}
