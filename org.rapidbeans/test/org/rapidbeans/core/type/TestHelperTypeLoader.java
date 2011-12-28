package org.rapidbeans.core.type;

import org.rapidbeans.core.type.RapidBeansTypeLoader;

public class TestHelperTypeLoader {
    
    /**
     * CAUTION: only for test purposes.
     */
    public static void clearBeanTypesGeneric() {
        RapidBeansTypeLoader.typeMapClearBeanGeneric();
        RapidBeansTypeLoader.typeMapClearRootElementBindingsGeneric();
    }
}
