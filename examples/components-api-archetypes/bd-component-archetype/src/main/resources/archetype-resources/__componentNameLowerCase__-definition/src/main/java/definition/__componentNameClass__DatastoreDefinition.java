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

import java.net.MalformedURLException;
import java.net.URL;

import ${packageTalend}.api.component.runtime.DependenciesReader;
import ${packageTalend}.api.component.runtime.JarRuntimeInfo;
import ${packageTalend}.api.exception.ComponentException;
import ${packageTalend}.common.dataset.DatasetProperties;
import ${packageTalend}.common.datastore.DatastoreDefinition;
import ${packageTalend}.${componentNameLowerCase}.definition.input.${componentName}InputDefinition;
import ${packageTalend}.${componentNameLowerCase}.definition.output.${componentName}OutputDefinition;
import ${packageDaikon}.definition.DefinitionImageType;
import ${packageDaikon}.definition.I18nDefinition;
import ${packageDaikon}.runtime.RuntimeInfo;

public class ${componentNameClass}DatastoreDefinition extends I18nDefinition implements DatastoreDefinition<${componentNameClass}DatastoreProperties> {

    public static final String RUNTIME = "org.talend.components.${componentNameLowerCase}.runtime.${componentNameClass}DatastoreRuntime";

    public static final String NAME = ${componentNameClass}ComponentFamilyDefinition.NAME + "Datastore";

    public ${componentNameClass}DatastoreDefinition() {
        super(NAME);
    }

    @Override
    public Class<${componentNameClass}DatastoreProperties> getPropertiesClass() {
        return ${componentNameClass}DatastoreProperties.class;
    }

    @Override
    public RuntimeInfo getRuntimeInfo(${componentNameClass}DatastoreProperties properties) {
        try {
            return new JarRuntimeInfo(new URL("mvn:org.talend.components/${componentNameLowerCase}-runtime"),
                    DependenciesReader.computeDependenciesFilePath("org.talend.components", "${componentNameLowerCase}-runtime"), RUNTIME);
        } catch (MalformedURLException e) {
            throw new ComponentException(e);
        }
    }

    @Deprecated
    @Override
    public String getImagePath(){
        return NAME + "_icon32.png";
    }

    @Override
    public String getImagePath(DefinitionImageType type) {
        switch (type) {
            case PALETTE_ICON_32X32:
                return NAME + "_icon32.png";
            case SVG_ICON:
                return NAME + ".svg";
        }
        return null;
    }

    @Override
    public String getIconKey() {
        return "${componentNameLowerCase}";
    }

    @Override
    public DatasetProperties createDatasetProperties(${componentNameClass}DatastoreProperties storeProp) {
        ${componentName}DatasetProperties setProp = new ${componentNameClass}DatasetProperties(${componentNameClass}DatasetDefinition.NAME);
        setProp.init();
        setProp.setDatastoreProperties(storeProp);
        return setProp;
    }

    @Override
    public String getInputCompDefinitionName() {
        return ${componentNameClass}InputDefinition.NAME;
    }

    @Override
    public String getOutputCompDefinitionName() {
        return ${componentNameClass}OutputDefinition.NAME;
    }

}
