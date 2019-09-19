package custom.apexrules;

import apex.jorje.semantic.ast.expression.MethodCallExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTMethodCallExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTReferenceExpression;
import net.sourceforge.pmd.lang.apex.ast.ASTSoqlExpression;
import net.sourceforge.pmd.lang.apex.rule.AbstractApexRule;

public class ApexAvoidSizeOfSoql extends AbstractApexRule {

	private static final String SIZE = "size";
	static final String ANY_METHOD = "*";

	@Override
	public Object visit(ASTMethodCallExpression methodCall, Object data) {

		/*List<ASTMethodCallExpression> methodCalls = node.findDescendantsOfType(ASTMethodCallExpression.class);*/

		/*for (ASTMethodCallExpression methodCall : methodCalls) {*/
			ASTSoqlExpression soql = methodCall.getFirstChildOfType(ASTReferenceExpression.class)
					.getFirstChildOfType(ASTSoqlExpression.class);
			if (isMethodName(methodCall, soql, SIZE)) {
				addViolation(data, methodCall);
			}

		/*}*/

		return data;

	}

	static boolean isMethodName(final ASTMethodCallExpression methodNode, final ASTSoqlExpression className,
			final String methodName) {
		final ASTReferenceExpression reference = methodNode.getFirstChildOfType(ASTReferenceExpression.class);

		return reference != null && reference.getFirstChildOfType(ASTSoqlExpression.class).equals(className)
				&& (methodName.equals(ANY_METHOD) || isMethodName(methodNode, methodName));
	}

	static boolean isMethodName(final ASTMethodCallExpression m, final String methodName) {
		return isMethodName(m.getNode(), methodName);
	}

	static boolean isMethodName(final MethodCallExpression m, final String methodName) {
		return m.getMethodName().equalsIgnoreCase(methodName);
	}
}
