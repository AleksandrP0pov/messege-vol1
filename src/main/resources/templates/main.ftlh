<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <div>
        <@l.logout />
        <span><a href="/user">User list</a> </span>
    </div>
    <div>
        <form method="post">
            <input type="text" name="text" placeholder="Entry massage"/>
            <input type="text" name="tag" placeholder="Tag" >
            <input type="hidden" name="_csrf" value="${_csrf.token}">
            <button type="submit">Add</button>
        </form>
    </div>

    <div>List of messages</div>
    <form method="get" action="/main">
        <input type="text" name="filter" value="${filter}">
        <button type="submit">To find</button>
    </form>
    <#list messages as messeges>
        <div>
            <b>${messeges.id}</b>
            <span>${messeges.text}</span>
            <i>${messeges.tag}</i>
            <strong>${messeges.authorName}</strong>
        </div>
    </#list>
<#--    <#else>-->
<#--    No messages-->
</@c.page>