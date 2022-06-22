import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
public class App {
    private ItemRepository itemRepository;
    private SalesPromotionRepository salesPromotionRepository;

    public App(ItemRepository itemRepository, SalesPromotionRepository salesPromotionRepository) {
        this.itemRepository = itemRepository;
        this.salesPromotionRepository = salesPromotionRepository;
    }

    public String bestCharge(List<String> inputs) {

        //TODO: write code here
        List<Item> items = this.itemRepository.findAll();
        int originalPriceSum = 0;
        int sum = 0;
        String message = "============= Order details =============\n";
        String msgOfHalfPricePromotion = "";
        int flag = 0;
        for (String s :inputs) {
            String[] itemOfinput = s.split("\\sx\\s");
            Optional<Item> item = items.stream().filter(x -> x.getId().equals(itemOfinput[0])).findFirst();
            item.orElseThrow(() -> new NullPointerException("don't have this item:"+itemOfinput[0]));
            double price = item.get().getPrice();
            if(this.salesPromotionRepository.findAll()
                    .get(1)
                    .getRelatedItems()
                    .stream()
                    .anyMatch(x -> x.equals(itemOfinput[0]))){
                if (flag == 0){
                    msgOfHalfPricePromotion = this.salesPromotionRepository.findAll().get(1).getDisplayName() +
                            " (" + item.get().getName();
                    flag = 1;
                }else {
                    msgOfHalfPricePromotion = msgOfHalfPricePromotion + ", " + item.get().getName();
                }
                sum += price * Integer.valueOf(itemOfinput[1]) / 2;
            }else {
                sum += price * Integer.valueOf(itemOfinput[1]);
            }

            message += String.format("%s x %s = %d yuan\n", item.get().getName(), itemOfinput[1], (int)item.get().getPrice() * Integer.valueOf(itemOfinput[1]));
            originalPriceSum += price * Integer.valueOf(itemOfinput[1]);
        }
        msgOfHalfPricePromotion += String.format("), saving %d yuan", originalPriceSum - sum);

        if (originalPriceSum >= 30 && (originalPriceSum - 6) <= sum){
            message += ("-----------------------------------\nPromotion used:\n" + this.salesPromotionRepository.findAll().get(0).getDisplayName() + ", saving 6 yuan\n");
            sum = originalPriceSum - 6;
        }else if (flag != 0){
            message += ("-----------------------------------\nPromotion used:\n" + msgOfHalfPricePromotion + "\n");
        }
        message += String.format("-----------------------------------\nTotal: %d yuan\n===================================",sum);

//        System.out.println(message);
        return message;
    }

//    public static void main(String[] args) {
//
//        Scanner scanner = new Scanner(System.in);
//        String input = scanner.nextLine();
//        String pattern = "ITEM(\\d+)\\sx\\s(\\d)";
//        Pattern r = Pattern.compile(pattern);
//        Matcher matcher = r.matcher(input);
//        List<String> list = new ArrayList<>();
//        while(matcher.find()){
//            list.add(matcher.group());
//        }
////        app.bestCharge();
//    }

}
