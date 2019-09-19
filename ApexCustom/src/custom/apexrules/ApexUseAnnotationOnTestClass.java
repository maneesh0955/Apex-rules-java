package custom.apexrules;

import java.util.List;

import apex.jorje.semantic.ast.modifier.Annotation;
import apex.jorje.semantic.ast.modifier.ModifierGroup;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTModifierNode;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClass;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public class ApexUseAnnotationOnTestClass extends AbstractApexRule {

	@Override
	public Object visit(ASTUserClass node, Object data) {
		boolean isMethodUserDefined;
		boolean isTestMethodFound = false;
		boolean isuserDefinedmethod = false;
		for (ASTMethod astClassMethod : node.findChildrenOfType(ASTMethod.class)) {
			isMethodUserDefined = astClassMethod.getNode().getMethodInfo().getGenerated().isUserDefined;

			if (isMethodUserDefined) {
				ASTModifierNode astMethodMod = astClassMethod.findChildrenOfType(ASTModifierNode.class).get(0);

				ModifierGroup mod = astMethodMod.getNode().getModifiers();
				if (mod.isTest()) {
					isTestMethodFound = true;
				}else{
					isuserDefinedmethod=true;
				}
			}
		}

		if (isTestMethodFound && !isuserDefinedmethod) {
			List<Annotation> anotations = node.getNode().getDefiningType().getModifiers().getAnnotations();
			if (anotations.size() > 0) {

				if (!"isTest".equalsIgnoreCase(
						node.getNode().getDefiningType().getModifiers().getAnnotations().get(0).getType().toString())) {
					addViolation(data, node);
					return data;
				}

			} else {
				addViolation(data, node);
				return data;
			}

		}
		return data;
	}
}
