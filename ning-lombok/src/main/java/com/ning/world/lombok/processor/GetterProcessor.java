package com.ning.world.lombok.processor;

import com.ning.world.lombok.annotation.Getter;
import com.ning.world.lombok.translator.GetterTreeTranslator;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * 处理自动生成类的getter方法{@link Getter}
 *
 * @author <a href="guotongning@58.com">Nicholas</a>
 * @since 1.0.0
 */
@SupportedAnnotationTypes({
        "com.ning.world.lombok.annotation.Getter",
//        "com.ning.world.lombok.annotation.Setter",
//        "com.ning.world.lombok.annotation.Data",
//        "com.ning.world.lombok.annotation.Builder",
//        "com.ning.world.lombok.annotation.Access",
})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class GetterProcessor extends AbstractProcessor {

    /**
     * 编译期打log用的
     */
    private Messager messager;
    /**
     * 待处理的抽象语法树
     */
    private JavacTrees trees;
    /**
     * 用于创建AST节点
     */
    private TreeMaker treeMaker;
    /**
     * 用于创建标识符
     */
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.trees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        this.treeMaker = TreeMaker.instance(context);
        this.names = Names.instance(context);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        roundEnv
                .getElementsAnnotatedWith(Getter.class)
                .forEach(element -> trees
                        .getTree(element)
                        .accept(new GetterTreeTranslator(messager, treeMaker, names))
                );
        return true;
    }
}
