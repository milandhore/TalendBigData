// ============================================================================
//
// Copyright (C) 2006-2017 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================

package ${package}.definition;

import org.junit.Ignore;
import org.junit.Test;

import ${packageDaikon}.runtime.RuntimeInfo;

import static org.junit.Assert.assertEquals;

public class ${componentNameClass}DatastoreDefinitionTest {

    private final ${componentNameClass}DatastoreDefinition datastoreDefinition = new ${componentNameClass}DatastoreDefinition();

    /**
    * Check {@link ${componentNameClass}DatastoreDefinition#getRuntimeInfo(${componentNameClass}DatastoreProperties) returns RuntimeInfo,
    * which runtime class name is "${package}.runtime.${componentNameClass}DatastoreRuntime"
    */
    @Test
    @Ignore("This can't work unless the runtime jar is already installed in maven!")
    public void testRuntimeInfo() {
        RuntimeInfo runtimeInfo = datastoreDefinition.getRuntimeInfo(null);
        assertEquals("org.talend.components.${componentNameLowerCase}.runtime.${componentNameClass}DatastoreRuntime", runtimeInfo.getRuntimeClassName());
    }
}
