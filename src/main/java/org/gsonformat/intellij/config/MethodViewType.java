package org.gsonformat.intellij.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kerry on 2020/10/18
 */

public enum MethodViewType {
    /**
     * lombok
     */
    LOMBOK,
    /**
     * set and get
     */
    SET_AND_GET;

    public static List<MethodViewType> of(MethodViewType... types) {
        if (types.length == 0) {
            return Collections.emptyList();
        } else if (types.length == 1) {
            return Collections.singletonList(types[0]);
        }
        List<MethodViewType> list = new ArrayList<>(types.length);
        Collections.addAll(list, types);
        return list;
    }
}
