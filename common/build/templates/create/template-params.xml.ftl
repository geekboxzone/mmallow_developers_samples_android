<#ftl>
<sample>
    <name>${sample.name}</name>
    <package>${sample.package}</package>


    <!--TODO: change minSdk if needed-->
    <minSdk>${sample.minSdk}</minSdk>


    <intro>
        <![CDATA[
        Introductory text that explains what the sample is intended to demonstrate. Edit
        in template-params.xml.
        ]]>
    </intro>

    <template src="base"/>
    <common src="logger"/>

</sample>
