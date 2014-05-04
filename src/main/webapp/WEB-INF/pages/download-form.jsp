<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<form:form method="POST" action="/dropbox-demo/execute-download">
   <table>
    <tr>
        <td><form:label path="title">Tile</form:label></td>
        <td><form:input path="title" /></td>
    </tr>
    <tr>
        <td><form:label path="url">URL</form:label></td>
        <td><form:input path="url" /></td>
    </tr>
    <tr>
        <td colspan="2">
            <input type="submit" value="Submit"/>
        </td>
    </tr>
</table>  
</form:form>
