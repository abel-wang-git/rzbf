
layui.define(['laypage', 'layer', 'form', 'pagesize'], function (exports) {
    var $ = layui.jquery,
        layer = layui.layer,
        form = layui.form(),
        laypage = layui.laypage;
    var laypageId = 'pageNav';

    initilData();
    //ҳ���ݳ�ʼ��
    //currentIndex����ǰҲ�±�
    //pageSize��ҳ������ÿҳ��ʾ��������
    function initilData() {
        var index = layer.load(1);
            layer.close(index);
            var pages = $("#pageTotale").val()
            var groups = $("#pageSize").val()
            laypage({
                cont: laypageId,
                pages: pages,
                groups: groups,
                skip: true,
                curr: $("#pageNumber").val(),
                jump: function(obj,first){//���ҳ��������¼�
                    if(first!=true){//�Ƿ��״ν���ҳ��
                        var currentPage = obj.curr;//��ȡ�����ҳ��
                        var option = {
                            type:"get",
                            data: {"pageNumber":currentPage-1},
                            dataType:"html",
                            success: function (data) {
                                $("#dataContent").html($(data).find("#dataContent"));
                            },
                            async: false//使用同步的方式,true为异步方式
                        };

                        $("#query").ajaxSubmit(option);
                        // window.location.href =$("#listUrl").val()+"?pageNumber="+(currentPage-1);
                    }
                }
            });
    }
    exports('datalist');
});