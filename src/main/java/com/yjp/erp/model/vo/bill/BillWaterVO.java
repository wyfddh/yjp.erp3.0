package com.yjp.erp.model.vo.bill;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * @Description
 * @Version 1.00
 * @Author wuyizhe@yijiupi.cn
 * @Date: 2019/4/15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BillWaterVO {

    private List<InventorySummary> inventorySummarys;

    private List<InboundWater> inboundWaters;

    private List<OutboundWater> outboundWaters;

    private Page page;

    private String msg;

    public List<InventorySummary> getInventorySummarys() {
        return inventorySummarys;
    }

    public void setInventorySummarys(List<InventorySummary> inventorySummarys) {
        this.inventorySummarys = inventorySummarys;
    }

    public List<InboundWater> getInboundWaters() {
        return inboundWaters;
    }

    public void setInboundWaters(List<InboundWater> inboundWaters) {
        this.inboundWaters = inboundWaters;
    }

    public List<OutboundWater> getOutboundWaters() {
        return outboundWaters;
    }

    public void setOutboundWaters(List<OutboundWater> outboundWaters) {
        this.outboundWaters = outboundWaters;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class InventorySummary {
        private String materialCode;
        private String endCost;
        private String category;
        private String productId;
        private String warehouse;
        private String increaseCost;
        private String decreaseCost;
        private String beginCost;
        private String month;
        private String productUnit;
        private String salesVolume;
        private String productName;
        private String org;
        private String conversion;
        private String year;
        private String decreaseNum;
        private String productStatus;
        private String id;
        private String beginNum;
        private String increaseNum;
        private String salesAmount;
        private String endNum;
        private String inventoryType;


        public String getInventoryType() {
            return inventoryType;
        }

        public void setInventoryType(String inventoryType) {
            this.inventoryType = inventoryType;
        }

        public String getMaterialCode() {
            return materialCode;
        }

        public void setMaterialCode(String materialCode) {
            this.materialCode = materialCode;
        }

        public String getEndCost() {
            return endCost;
        }

        public void setEndCost(String endCost) {
            this.endCost = endCost;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(String warehouse) {
            this.warehouse = warehouse;
        }

        public String getIncreaseCost() {
            return increaseCost;
        }

        public void setIncreaseCost(String increaseCost) {
            this.increaseCost = increaseCost;
        }

        public String getDecreaseCost() {
            return decreaseCost;
        }

        public void setDecreaseCost(String decreaseCost) {
            this.decreaseCost = decreaseCost;
        }

        public String getBeginCost() {
            return beginCost;
        }

        public void setBeginCost(String beginCost) {
            this.beginCost = beginCost;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getProductUnit() {
            return productUnit;
        }

        public void setProductUnit(String productUnit) {
            this.productUnit = productUnit;
        }

        public String getSalesVolume() {
            return salesVolume;
        }

        public void setSalesVolume(String salesVolume) {
            this.salesVolume = salesVolume;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getConversion() {
            return conversion;
        }

        public void setConversion(String conversion) {
            this.conversion = conversion;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getDecreaseNum() {
            return decreaseNum;
        }

        public void setDecreaseNum(String decreaseNum) {
            this.decreaseNum = decreaseNum;
        }

        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getBeginNum() {
            return beginNum;
        }

        public void setBeginNum(String beginNum) {
            this.beginNum = beginNum;
        }

        public String getIncreaseNum() {
            return increaseNum;
        }

        public void setIncreaseNum(String increaseNum) {
            this.increaseNum = increaseNum;
        }

        public String getSalesAmount() {
            return salesAmount;
        }

        public void setSalesAmount(String salesAmount) {
            this.salesAmount = salesAmount;
        }

        public String getEndNum() {
            return endNum;
        }

        public void setEndNum(String endNum) {
            this.endNum = endNum;
        }
    }

    public static class InboundWater {
        private String inboundNum;
        private String product;
        private String billId;
        private String inboundTotalPrice;
        private String createTime;
        private String warehouse;
        private String inboundTime;
        private String inboundPrice;
        private String org;
        private String inboundWaterNumber;
        private String inventoryId;
        private String manufactureDate;
        private String batchInboundNumber;
        private String productUnit;
        private String inboundType;

        public String getInboundType() {
            return inboundType;
        }

        public void setInboundType(String inboundType) {
            this.inboundType = inboundType;
        }

        public String getProductUnit() {
            return productUnit;
        }

        public void setProductUnit(String productUnit) {
            this.productUnit = productUnit;
        }

        public String getInboundNum() {
            return inboundNum;
        }

        public void setInboundNum(String inboundNum) {
            this.inboundNum = inboundNum;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getInboundTotalPrice() {
            return inboundTotalPrice;
        }

        public void setInboundTotalPrice(String inboundTotalPrice) {
            this.inboundTotalPrice = inboundTotalPrice;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(String warehouse) {
            this.warehouse = warehouse;
        }

        public String getInboundTime() {
            return inboundTime;
        }

        public void setInboundTime(String inboundTime) {
            this.inboundTime = inboundTime;
        }

        public String getInboundPrice() {
            return inboundPrice;
        }

        public void setInboundPrice(String inboundPrice) {
            this.inboundPrice = inboundPrice;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getInboundWaterNumber() {
            return inboundWaterNumber;
        }

        public void setInboundWaterNumber(String inboundWaterNumber) {
            this.inboundWaterNumber = inboundWaterNumber;
        }

        public String getInventoryId() {
            return inventoryId;
        }

        public void setInventoryId(String inventoryId) {
            this.inventoryId = inventoryId;
        }

        public String getManufactureDate() {
            return manufactureDate;
        }

        public void setManufactureDate(String manufactureDate) {
            this.manufactureDate = manufactureDate;
        }

        public String getBatchInboundNumber() {
            return batchInboundNumber;
        }

        public void setBatchInboundNumber(String batchInboundNumber) {
            this.batchInboundNumber = batchInboundNumber;
        }
    }

    public static class OutboundWater {

        private String org;
        private String quantity;
        private String createTime;
        private String product;
        private String inventoryId;
        private String totalAmount;
        private String batchInboundNumber;
        private String price;
        private String billId;
        private String outboundDate;
        private String warehouse;
        private String outboundSummery;
        private String productUnit;

        public String getProductUnit() {
            return productUnit;
        }

        public void setProductUnit(String productUnit) {
            this.productUnit = productUnit;
        }

        public String getOrg() {
            return org;
        }

        public void setOrg(String org) {
            this.org = org;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getProduct() {
            return product;
        }

        public void setProduct(String product) {
            this.product = product;
        }

        public String getInventoryId() {
            return inventoryId;
        }

        public void setInventoryId(String inventoryId) {
            this.inventoryId = inventoryId;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getBatchInboundNumber() {
            return batchInboundNumber;
        }

        public void setBatchInboundNumber(String batchInboundNumber) {
            this.batchInboundNumber = batchInboundNumber;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getBillId() {
            return billId;
        }

        public void setBillId(String billId) {
            this.billId = billId;
        }

        public String getOutboundDate() {
            return outboundDate;
        }

        public void setOutboundDate(String outboundDate) {
            this.outboundDate = outboundDate;
        }

        public String getWarehouse() {
            return warehouse;
        }

        public void setWarehouse(String warehouse) {
            this.warehouse = warehouse;
        }

        public String getOutboundSummery() {
            return outboundSummery;
        }

        public void setOutboundSummery(String outboundSummery) {
            this.outboundSummery = outboundSummery;
        }
    }

    public static class Page {
        private Integer pageNo;
        private Integer pageSize;
        private Integer total;

        public Integer getPageNo() {
            return pageNo;
        }

        public void setPageNo(Integer pageNo) {
            this.pageNo = pageNo;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }
}



