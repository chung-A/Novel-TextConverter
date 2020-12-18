<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>AdminExcel</title>
</head>
<body>
      <!-- BEGIN CONTENT -->
      <div class="page-content-wrapper">
            <!-- BEGIN CONTENT BODY -->
            <div class="page-content">
                  <div class="page-head">
                        <!-- BEGIN PAGE TITLE -->
                        <div class="page-title">
                              <h1> <span id="title"></span><small id="titleSmall"></small> </h1>
                        </div>
                  <!-- END PAGE TITLE -->
                  </div>
                  <div class="col-lg-10 well">
                        <div class="row">
                              <input value="텍스트파일 등록" type="button" onclick="location.href='excel/textUpload.do'">
                        </div>
                  </div>
                  <!-- END container -->
            </div>
            <!-- END CONTENT BODY -->
      </div>
      <!-- END CONTENT -->
</body>
</html>