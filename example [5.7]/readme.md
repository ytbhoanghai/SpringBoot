Có thể lấy HttpServletRequest như sau:<br>
RequestContextHolder.getRequestAttributes() > return RequestAttribute > ép kiểu sang ServletRequestAttributes > getRequest()<br><br>
Có thể lấy LocalResolver như sau:<br>
Sử dụng phương thức static getLocalResolver() trong class RequestContextUtils<br><br>
Danh sách hằng số thuộc tính hibernate<br>
https://docs.jboss.org/hibernate/orm/4.3/javadocs/constant-values.html
