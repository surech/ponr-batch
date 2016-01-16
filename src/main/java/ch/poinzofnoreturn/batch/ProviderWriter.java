package ch.poinzofnoreturn.batch;

import ch.poinzofnoreturn.batch.model.db.ProviderEntity;
import ch.poinzofnoreturn.batch.repo.ProviderRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by surech on 16.01.16.
 */
public class ProviderWriter implements ItemWriter<ProviderEntity> {

    @Autowired
    private ProviderRepository providerRepository;

    @Override
    public void write(List<? extends ProviderEntity> list) throws Exception {
        // Jeden Provider einzeln speichern
        for (ProviderEntity providerEntity : list) {
            saveProvider(providerEntity);
        }
    }

    private void saveProvider(ProviderEntity provider) {
        // Bestehender Provider suchen
        List<ProviderEntity> existingProviders = providerRepository.findByPoinzId(provider.getPoinzId());

        if (!existingProviders.isEmpty()) {
            ProviderEntity existingProvider = existingProviders.get(0);
            existingProvider.setName(bt(provider.getName(), 255));
            existingProvider.setStreet(provider.getStreet());

            providerRepository.save(existingProvider);
        } else {
            // Provider speichern
            providerRepository.save(provider);
        }
    }

    public String bt(String value, int lenght) {
        return StringUtils.substring(value, 0, lenght);
    }
}
