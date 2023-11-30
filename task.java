import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

interface Structure {
    // zwraca dowolny element o podanym kolorze
    Optional<Block> findBlockByColor(String color);

    // zwraca wszystkie elementy z danego materiału
    List<Block> findBlocksByMaterial(String material);

    // zwraca liczbę wszystkich elementów tworzących strukturę
    int count();
}

public class Wall implements Structure {
    private List<Block> blocks;

    public Wall(List<Block> blocks) {
        this.blocks = blocks;
    }

    @Override
    public Optional<Block> findBlockByColor(String color) {
        for (Block block : blocks) {
            if (block.getColor().equalsIgnoreCase(color)) { // block nie jest instancja CompositeBlock
                return Optional.of(block);
            }
            if (block instanceof CompositeBlock) {
                CompositeBlock compositeBlock = (CompositeBlock) block;
                Optional<Block> foundBlock = compositeBlock.getBlocks().stream()
                        .filter(innerBlock -> innerBlock.getColor().equalsIgnoreCase(color))
                        .findFirst();
                if (foundBlock.isPresent()) {
                    return foundBlock;
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Block> findBlocksByMaterial(String material) {
        List<Block> result = new ArrayList<>();
        for (Block block : blocks) {
            if (block.getMaterial().equalsIgnoreCase(material)) {
                result.add(block);
            }
            if (block instanceof CompositeBlock) {
                CompositeBlock compositeBlock = (CompositeBlock) block;
                List<Block> foundBlocks = compositeBlock.getBlocks().stream()
                        .filter(innerBlock -> innerBlock.getMaterial().equalsIgnoreCase(material))
                        .collect(Collectors.toList());

                result.addAll(foundBlocks);

                // note: potencjalne rozszerzenie dla obsługi interfejsu w którym blocks byłoby
                // zdefiniowane jako List<CompositeBlock>
                // można za pomocą rekurencjii policzyć wewnętrzne bloki

            }
        }
        return result;
    }

    @Override
    public int count() {
        return blocks.size();
    }
}

interface Block {
    String getColor();

    String getMaterial();
}

interface CompositeBlock extends Block {
    List<Block> getBlocks();
}
