package org.rapidbeans.core.type;


public class TestHelperTypeLoader {
    
    /**
     * CAUTION: only for test purposes.
     */
    public static void clearBeanTypesGeneric() {
        RapidBeansTypeLoader.typeMapClearBeanGeneric();
        RapidBeansTypeLoader.typeMapClearRootElementBindingsGeneric();
    }
}
