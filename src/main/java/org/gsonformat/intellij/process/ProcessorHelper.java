package org.gsonformat.intellij.process;

import org.gsonformat.intellij.entity.ConvertLibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kerry on 2020/10/18
 */

public class ProcessorHelper {

    private static final Map<ConvertLibrary, Processor> PROCESSOR_MAP;

    static {
        PROCESSOR_MAP = new HashMap<>();
        PROCESSOR_MAP.put(ConvertLibrary.NONE, new NoneProcessor());
        PROCESSOR_MAP.put(ConvertLibrary.GSON, new GsonProcessor());
        PROCESSOR_MAP.put(ConvertLibrary.JACKSON, new JacksonProcessor());
        PROCESSOR_MAP.put(ConvertLibrary.FAST_JSON, new FastJsonProcessor());
        PROCESSOR_MAP.put(ConvertLibrary.AUTO_VALUE, new AutoValueProcessor());
        PROCESSOR_MAP.put(ConvertLibrary.LOGAN_SQUARE, new LoganSquareProcessor());
        PROCESSOR_MAP.put(ConvertLibrary.OTHER, new OtherProcessor());
    }


    public static Processor getProcessor(ConvertLibrary convertLibrary) {
        return PROCESSOR_MAP.get(convertLibrary);
    }

}
