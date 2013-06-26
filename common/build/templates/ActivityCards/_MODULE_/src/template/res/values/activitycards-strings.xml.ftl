<#include "/ActivityCardsCommon.ftli">

<resources>
<#list sample.activity as activity>
    <string name="<@make_activity_res activity "title"/>">${activity.title!"activity.title"}</string>
    <string name="<@make_activity_res activity "description"/>">${activity.description!"activity.description"}</string>
</#list>
</resources>