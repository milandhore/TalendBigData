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

package ${package}.definition.input;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;

import ${packageTalend}.api.component.AbstractComponentDefinition;
import ${packageTalend}.api.component.ConnectorTopology;
import ${packageTalend}.api.component.runtime.DependenciesReader;
import ${packageTalend}.api.component.runtime.ExecutionEngine;
import ${packageTalend}.api.component.runtime.JarRuntimeInfo;
import ${packageTalend}.api.exception.ComponentException;
import ${packageTalend}.api.properties.ComponentProperties;
import ${packageTalend}.${componentNameLowerCase}.definition.${componentNameClass}ComponentFamilyDefinition;
import ${packageDaikon}.properties.property.Property;
import ${packageDaikon}.runtime.RuntimeInfo;


public class ${componentNameClass}InputDefinition extends AbstractComponentDefinition {

    public static final String NAME = ${componentNameClass}ComponentFamilyDefinition.NAME + "Input";

    public static final String RUNTIME = "org.talend.components.${componentNameLowerCase}.runtime.${componentNameClass}InputRuntime";

    public ${componentNameClass}InputDefinition() {
        super(NAME, ExecutionEngine.BEAM);
    }

    @Override
    public Class<? extends ComponentProperties> getPropertyClass() {
        return ${componentNameClass}InputProperties.class;
    }

    @Override
    public RuntimeInfo getRuntimeInfo(ExecutionEngine engine, ComponentProperties properties, ConnectorTopology topology) {
        assertEngineCompatibility(engine);
        assertConnectorTopologyCompatibility(topology);
        try {
            return new JarRuntimeInfo(new URL("mvn:org.talend.components/${componentNameLowerCase}-runtime"),
                    DependenciesReader.computeDependenciesFilePath("org.talend.components", "${componentNameLowerCase}-runtime"), RUNTIME);
        } catch (MalformedURLException e) {
            throw new ComponentException(e);
        }
    }

    public Property[] getReturnProperties() {
        return new Property[]{};
    }

    public Set<ConnectorTopology> getSupportedConnectorTopologies() {
        return EnumSet.of(ConnectorTopology.OUTGOING);
    }

    @Override
    public String getIconKey() {
        return "${componentNameLowerCase}";
    }
}
