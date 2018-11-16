// mo_yq5 在编辑器中定义一个插件，注意，插件名称与插件目录名要保持一致
CKEDITOR.plugins.add( 'syntaxhighlighter', {
    // mo_yq5 初始化插件
    init: function( editor ) {
        //mo_yq5 引入对话框的定义，所引入的文件的内容是对话框的具体逻辑定义
        CKEDITOR.dialog.add( 'shDialog', this.path + 'dialogs/syntaxhighlighter.js' );
        //mo_yq5 在编辑器中追加一条指令
        editor.addCommand( 'shDialog', new CKEDITOR.dialogCommand( 'shDialog' ) );
        //mo_yq5 在编辑器中追加一个工具栏按钮
        editor.ui.addButton( 'shButton', {
            //mo_yq5 鼠标移到按钮上时的提示文本
            label: '插入代码',
            //mo_yq5 点击按钮时要执行的指令，即上面添加的指令
            command: 'shDialog',
            //mo_yq5 在编辑器工具栏中嵌入按钮的方式
            toolbar: 'insert',
            //mo_yq5 工具栏上按钮的图标
            icon: this.path + 'icons/syntaxhighlighter.png'
        });
          
    }
});