public class Main {
    public static void main(String[] args) {
        CustomerDAO dao = new CustomerDAO();

        try {
            // 添加
            Customer c1 = new Customer("U0001", "张三", "13812345678", "zhangsan@mail.com", "北京", "1");
            dao.addCustomer(c1);
            System.out.println("添加成功！");

            // 查询
            System.out.println("所有客户：");
            for (Customer c : dao.getAllCustomers()) {
                System.out.println(c);
            }

            // 修改
            c1.setAddress("上海");
            dao.updateCustomer(c1);
            System.out.println("更新成功！");

            // 删除
            dao.deleteCustomer("U0001");
            System.out.println("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
