package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.db.EmfProvider;

public class EmfExtension implements SuiteExtension {

    @Override
    public void afterSuite() {
        EmfProvider.INSTANCE.storedEmf().forEach(
                emf -> emf.close()
        );
    }
}
