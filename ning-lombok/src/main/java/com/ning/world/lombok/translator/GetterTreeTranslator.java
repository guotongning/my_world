package com.ning.world.lombok.translator;

import com.sun.source.tree.Tree;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * 构建新的语法树 {@link TreeTranslator}
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
public class GetterTreeTranslator extends TreeTranslator {

    private final Messager messager;
    private final TreeMaker treeMaker;
    private final Names names;

    public GetterTreeTranslator(Messager messager, TreeMaker treeMaker, Names names) {
        this.messager = messager;
        this.treeMaker = treeMaker;
        this.names = names;
    }

    @Override
    public void visitClassDef(JCTree.JCClassDecl jcClassDecl) {
        List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
        //保存成员变量
        for (JCTree tree : jcClassDecl.defs) {
            if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
            }
        }
        //遍历，打日志，修改方法定义。
        jcVariableDeclList.forEach(jcVariableDecl -> {
            messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
            jcClassDecl.defs = jcClassDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
        });
        super.visitClassDef(jcClassDecl);
    }

    // 构建Getter方法定义
    private JCTree makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        statements
                .append(treeMaker
                        .Return(treeMaker
                                .Select(treeMaker
                                        .Ident(names
                                                .fromString("this")), jcVariableDecl.getName()
                                )
                        )
                );
        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
        return treeMaker
                .MethodDef(
                        treeMaker.Modifiers(Flags.PUBLIC),
                        getNewMethodName(jcVariableDecl.getName()),
                        jcVariableDecl.vartype,
                        List.nil(),
                        List.nil(),
                        List.nil(),
                        body,
                        null
                );
    }

    private Name getNewMethodName(Name name) {
        String nameDef = name.toString();
        return names.fromString("get" + nameDef.substring(0, 1).toUpperCase() + nameDef.substring(1, name.length()));
    }

}
