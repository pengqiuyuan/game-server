<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
int current =  page.getNumber() + 1;
int begin = Math.max(1, current - paginationSize/2);
int end = Math.min(begin + (paginationSize - 1), page.getTotalPages());
int size = page.getSize();
request.setAttribute("size", size);
request.setAttribute("current", current);
request.setAttribute("begin", begin);
request.setAttribute("end", end);
%>

<div class="pagination">
	<ul>
		 <% if (page.hasPreviousPage()){%>
               	<li><a href="javascript:page(1,${sortType},${searchParams});">&lt;&lt;</a></li>
                <li><a href="javascript:page(${current-1},${sortType},${searchParams});">&lt;</a></li>
         <%}else{%>
                <li class="disabled"><a href="#">&lt;&lt;</a></li>
                <li class="disabled"><a href="#">&lt;</a></li>
         <%} %>
 
		<c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="javascript:page(${i},${sortType},${searchParams});">${i}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="javascript:page(${i},${sortType},${searchParams});">${i}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
	  
	  	 <% if (page.hasNextPage()){%>
               	<li><a href="javascript:page(${current+1},${sortType},${searchParams});">&gt;</a></li>
                <li><a href="javascript:page(${page.totalPages},${sortType},${searchParams});">&gt;&gt;</a></li>
         <%}else{%>
                <li class="disabled"><a href="#">&gt;</a></li>
                <li class="disabled"><a href="#">&gt;&gt;</a></li>
         <%} %>

	</ul>
	<select id="pageSize" name="pageSize" style="width:40px ">
	<option value="5"  ${size == 5 ? "select":""} >5</option>
	<option value="10" ${size == 10 ? "select":""} >10</option>
	<option value="15" ${size == 15 ? "select":""} >15</option>
	<option value="20" ${size == 20 ? "select":""} >20</option>
	</select>
</div>

<script type="text/javascript">


</script>

