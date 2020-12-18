<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>UploadExcel</title>
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
                              <form id="excelUpForm" method="post" action="/excel/textUpload.do" role="form" enctype="multipart/form-data">
                                    <div class="col-sm-12">
                                          <div class="row" id="regGoodsImgArea">
                                                <div class="col-sm-4">
                                                      <label>텍스트 파일 업로드 (UTF-8인코딩 할 것)</label>
                                                      <input id="text" name="text" class="file" type="file"
                                                                  multiple data-show-upload="false" data-show-caption="true"/>
                                                </div>
                                          </div>
                                    </div>
                                    <input value="등록" type="submit">
                              </form>
                        </div>
                  </div>
                  <!-- END container -->
            </div>
            <!-- END CONTENT BODY -->
      </div>
      <!-- END CONTENT -->
</body>
</html>