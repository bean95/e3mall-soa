<html>
<head>
<title>Stu</title>
</head>
<body>
表格：</br>
<#list stuList as s>
	<#if s.age gt 23>
	<span style="color:red">
	<#else>
	<span style="color:green">
	</#if>
		${s_index}&nbsp;&nbsp;${s.name}&nbsp;&nbsp;${s.age}&nbsp;&nbsp;${s.address}
		${(s.birth?string('yyyy-MM-dd HH:mm:ss'))!}
		</br>
	</span>
</#list>
</body>