class Hovedprogram {

	public static void main (String[] args) {
		String[] argumenter = {""};
		if(args.length == 0) {
			System.out.println("          Dette programmet kan ogsaa startes med parametere:");
			System.out.println("        > To heltall mellom 4 og 40 som angir rader og kolonner");
			System.out.println("        > (Du maa kanskje justere vindusstoerrelse for aa se alt innhold.)\n");
		} 
		else if (args.length == 2) {
			for (int i = 0; i < 2; i++) {
				try {
					Integer[] oppsett = {null, null};
					oppsett[i] = Integer.parseInt(args[i]);
					// vil ikke tillate for store eller smaa tall som argumenter
					if (oppsett[i] > 40 || oppsett[i] < 4) {
						System.out.println("FEIL: Dimensjonen maa vaere mellom 4x4 og 40x40.\n(Eller start med default oppsett ved aa droppe argumenter.)");
						System.exit(1);
					}
					argumenter = args;
				} catch (NumberFormatException e) {
					System.err.println("FEIL: Det er ikke oppgitt to heltall som argumenter for rutenett.\n");
					System.exit(1);
				}
			}
		} else { 
			argumenter = args;
		}
		Kontroller kontroller = new Kontroller(argumenter);
		kontroller.init();
	}
}
