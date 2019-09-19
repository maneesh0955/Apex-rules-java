package custom.apexrules;

import net.sourceforge.pmd.lang.apex.ast.ASTBlockStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTLiteralExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTMethod;
import net.sourceforge.pmd.lang.apex.ast.ASTModifierNode;
import net.sourceforge.pmd.lang.apex.ast.ASTReturnStatement;
import net.sourceforge.pmd.lang.apex.ast.ASTUserClass;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;
import net.sourceforge.pmd.lang.ast.Node;

/**
 * 
 * @author 688515
 *
 */
public class ApexEmptyMethodInAbstractClassShouldBeAbstract extends AbstractApexRule {

	@Override
	public Object visit(ASTUserClass node, Object data) {

		ASTModifierNode nod = (ASTModifierNode) node.findChildrenOfType(ASTModifierNode.class).get(0);
		if (nod.isAbstract()) {
			for (ASTMethod m : node.findDescendantsOfType(ASTMethod.class)) {
				if (m.jjtGetNumChildren() == 2) {
					if (isEmptyBlockStatement(m.jjtGetChild(1))) {
						addViolation(data, m);
					}
				}
			}
		}

		return data;

	}

	boolean isEmptyBlockStatement(Node parent) {

		ASTBlockStatement bStmt = (ASTBlockStatement) parent;
		if (bStmt instanceof ASTBlockStatement) {
			if (bStmt.jjtGetNumChildren() > 1) {
				return false;
			} else if (bStmt.jjtGetNumChildren() == 1) {
				if (bStmt.findChildrenOfType(ASTReturnStatement.class) != null
						&& bStmt.findChildrenOfType(ASTReturnStatement.class).size() > 0) {
					ASTReturnStatement rs = bStmt.findChildrenOfType(ASTReturnStatement.class).get(0);
					if(rs.findChildrenOfType(ASTLiteralExpression.class) != null && rs.findChildrenOfType(ASTLiteralExpression.class).size() > 0){
						if(rs.findChildrenOfType(ASTLiteralExpression.class).get(0).getNode().toString().equals("null")){
							return true;
						}else{
							return false;
						}
					}
					return true;
				} else {
					return false;
				}
			} else if (bStmt.jjtGetNumChildren() == 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
