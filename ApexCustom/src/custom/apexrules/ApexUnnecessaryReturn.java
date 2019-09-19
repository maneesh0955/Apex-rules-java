package custom.apexrules;

import net.sourceforge.pmd.lang.apex.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClass;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

/**
 * 
 * @author 688515
 *
 */
public class ApexUnnecessaryReturn extends AbstractApexRule {
	
	@Override
	public Object visit(ASTUserClass node, Object data) {

		for (ASTReturnStatement li : node.findDescendantsOfType(ASTReturnStatement.class)) {

			if (li.jjtGetParent().getClass().equals(ASTBlockStatement.class)
					&& li.jjtGetParent().jjtGetParent().getClass().equals(ASTMethod.class)) {
				ASTMethod n = (ASTMethod) li.jjtGetParent().jjtGetParent();
				if ("void".equalsIgnoreCase(n.getNode().getMethodInfo().getReturnType().toString()))
					addViolation(data, li);
			}

		}

		return data;

	}
}
