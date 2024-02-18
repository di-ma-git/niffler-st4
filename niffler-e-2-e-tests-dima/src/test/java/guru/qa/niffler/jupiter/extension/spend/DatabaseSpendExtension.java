package guru.qa.niffler.jupiter.extension.spend;

import guru.qa.niffler.db.model.CategoryEntity;
import guru.qa.niffler.db.model.SpendEntity;
import guru.qa.niffler.db.repository.category.CategoryRepository;
import guru.qa.niffler.db.repository.category.CategoryRepositoryHibernate;
import guru.qa.niffler.db.repository.spend.SpendRepository;
import guru.qa.niffler.db.repository.spend.SpendRepositoryHibernate;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import guru.qa.niffler.model.SpendJson;
import org.junit.jupiter.api.extension.ExtendWith;

public class DatabaseSpendExtension extends Hw9SpendExtension {

    private SpendRepository spendRepository = new SpendRepositoryHibernate();
    private CategoryRepository categoryRepository = new CategoryRepositoryHibernate();

    @Override
    SpendJson create(SpendJson spend) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setUsername(spend.username());
        categoryEntity.setCategory(spend.category());
        CategoryEntity createdCategory = categoryRepository.create(categoryEntity);

        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setUsername(spend.username());
        spendEntity.setCurrency(spend.currency());
        spendEntity.setSpendDate(spend.spendDate());
        spendEntity.setAmount(spend.amount());
        spendEntity.setDescription(spend.description());
        spendEntity.setCategory(createdCategory);
        SpendEntity createdSpend = spendRepository.create(spendEntity);

        return spend;
    }
}
