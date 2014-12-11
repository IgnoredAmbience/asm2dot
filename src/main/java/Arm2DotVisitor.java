import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;

import parser.ArmAsmBaseVisitor;
import parser.ArmAsmLexer;
import parser.ArmAsmParser;
import parser.ArmAsmParser.InstrBranchContext;
import parser.ArmAsmParser.InstrOtherContext;
import parser.ArmAsmParser.LabelContext;
import parser.ArmAsmParser.ProgrContext;

public class Arm2DotVisitor extends ArmAsmBaseVisitor<Void> {
	private String prevNode;
	private int counter = 0;
	private StringBuilder instrs = new StringBuilder(); 
	
	private String genLabel() {
		return "node" + counter++;
	}
	
	private void outputInstrs() {
		if(instrs.length() != 0) {
			String label = genLabel();
			outputNode(label, instrs.toString(), "");
			outputEdge(label);
			prevNode = label;
			instrs = new StringBuilder();
		}
	}
	
	private void outputNode(String id, String label, String attrs) {
		System.out.println(id + " [label=\"" + label + "\"," + attrs + "];");
	}
	
	private void outputEdge(String to) {
		outputEdge(prevNode, to, "");
	}
	
	private void outputEdge(String fro, String to, String attrs) {
		System.out.println(fro + " -> " + to + " [" + attrs + "];");
	}
	
	@Override
	public Void visitProgr(ProgrContext ctx) {
		System.out.println("digraph {");
		prevNode = "START";
		super.visitProgr(ctx);
		outputEdge("END");
		System.out.println("}");
		return null;
	}

	@Override
	public Void visitLabel(LabelContext ctx) {
		outputInstrs();
		String label = ctx.ID().getText();
		outputNode(label, ctx.getText(), "shape=box");
		outputEdge(label);
		prevNode = label;
		return null;
	}
	
	@Override
	public Void visitInstrBranch(InstrBranchContext ctx) {
		outputInstrs();
		String id = genLabel();
		outputNode(id, ctx.getText(), "shape=diamond");
		outputEdge(id);
		outputEdge(id, ctx.ID().getText(), "arrowType=empty");
		prevNode = id;
		return null;
	}


	@Override
	public Void visitInstrOther(InstrOtherContext ctx) {
		instrs.append(ctx.getText() + "\\n");
		return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(args[0]));
		ArmAsmLexer l = new ArmAsmLexer(input);
		TokenStream tks = new CommonTokenStream(l);
		ArmAsmParser p = new ArmAsmParser(tks);
		
		Arm2DotVisitor v = new Arm2DotVisitor();
		
		v.visit(p.progr());
	}
}
