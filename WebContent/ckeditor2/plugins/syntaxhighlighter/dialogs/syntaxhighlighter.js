//mo_yq5 定义一个对话框。
CKEDITOR.dialog.add( 'shDialog', function ( editor ) {
    return {
        //mo_yq5 对话框标题。
        title: '插入代码',
        //mo_yq5 对话框最小宽度。
        minWidth: 400,
        //mo_yq5 对话框最小高度。
        minHeight: 200,
        //mo_yq5 对话框内容,一个元素对应一个选项卡。
        contents: [
            {
                //mo_yq5 选项卡标识id。
                id: 'tab_hightlighterCode',
                //mo_yq5 选项卡标题。
                label: '代码内容',
                //mo_yq5 选项卡内容，一个元素对应一个表单项。
                elements: [
                    {   
                        //mo_yq5 表单类型。
                        type: 'select',
                        //mo_yq5 表单名。
                        label: '语言:',
                        //mo_yq5 表单标识id。
                        id: 'lang',
                        //mo_yq5 是否为必填项。
                        required: true,
                        //mo_yq5 表单默认值。
                        'default': 'java',
                        items: [['ActionScript3', 'as3'], ['Bash/shell', 'bash'], ['ColdFusion', 'cf'], ['C#', 'csharp'], ['C++', 'cpp'], ['CSS', 'css'], ['Delphi', 'delphi'], ['Diff', 'diff'], ['Groovy', 'groovy'], ['JavaScript', 'js'], ['Java', 'java'], ['JavaFX', 'jfx'], ['Perl', 'perl'], ['PHP', 'php'], ['Plain Text', 'plain'], ['PowerShell', 'ps'], ['Python', 'py'], ['Ruby', 'rails'], ['Scala', 'scala'], ['SQL', 'sql'], ['Visual Basic', 'vb'], ['XML', 'xml']]
                     },
                    {
                        type: 'textarea',
                        style: 'width:418px;height:250px',
                        label: '代码:',
                        required: true,
                        id: 'code',
                        rows: 13,
                        'default': ''
                    }
                ]
            }
        ],
        //mo_yq5 定义提交事件。
        onOk: function(){
            //mo_yq5 从下拉列表获取到的语言类型。
            var lang = this.getValueOf('tab_hightlighterCode', 'lang');
            //mo_yq5 将要被渲染成高亮显示的内容（代码）。
            var code = this.getValueOf('tab_hightlighterCode', 'code');
            //mo_yq5 在当前页面上创建的一个用于存放代码的容器，因为渲染器SyntaxHighlighter是取页面内容来渲染的。该容器不显示。
            if($("#brushContainer").length==0){
                $("body").append('<div id="brushContainer" style="display:none;"></div>');
            }
            //mo_yq5 主要目的是将‘<’这样的标签转换成实体。
            code = CKEDITOR.tools.htmlEncode(code);
            //mo_yq5 将要渲染的代码放入容器。
            $("#brushContainer").html('<pre class="brush:'+ lang +';">'+code+'</pre>');
            //mo_yq5 将渲染各种语言的类准备好 。
            SyntaxHighlighter.autoloader.apply(null,shBrushArray);
            //mo_yq5 开始渲染。
            SyntaxHighlighter.all();
            //mo_yqr@163.com 等待2秒（因为渲染是异步的，可能需要点时间），将渲染后的内容插入编辑器。
            setTimeout(function(){
                //mo_yq5 将类似‘<’等的实体标识为文本内容，不然下次用编辑器打开时会被认为是html标签，造成编辑器上看不到该字符。
                code = $("#brushContainer").html().replace(/(&){1}(lt;){1}/g,"<code>&"+"lt;</code>");
                code = code.replace(/(&){1}(amp;){1}/g,"<code>&"+"amp;</code>");
                //mo_yq5 将渲染后的内容插入编辑器,加‘<p/>’是为了让光标移出被渲染后的代码块。
                editor.insertHtml(code+"<p/>");
            },2000);
        }
    };
});