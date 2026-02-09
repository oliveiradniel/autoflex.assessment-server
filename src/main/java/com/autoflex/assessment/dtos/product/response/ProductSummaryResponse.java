package com.autoflex.assessment.dtos.product.response;

public class ProductSummaryResponse {

    private Integer total;

    private Integer active;

    private Integer inactive;

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }

    public Integer getActive() { return active; }
    public void setActive(Integer active) { this.active = active; }

    public Integer getInactive() { return inactive; }
    public void setInactive(Integer inactive) { this.inactive = inactive; }
}
