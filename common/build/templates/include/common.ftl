<#-- Add the appropriate copyright header -->
<#if meta.outputFile?ends_with("java")>
    <#include "c-style-copyright.ftl">
<#elseif meta.outputFile?ends_with("xml")>
    <#include "xml-style-copyright.ftl">
</#if>
