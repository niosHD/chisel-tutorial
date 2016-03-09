tut_firs    := $(addsuffix .fir, $(executables))

firs: $(tut_firs)

firrtl	?= firrtl

.PHONY:	firs

$(objdir)/%.out: $(srcdir)/%.scala
	"$(SBT)" $(SBT_FLAGS) "run $(notdir $(basename $<)) --test --targetDir $(objdir)" | tee $@

$(objdir)/%.v:	$(objdir)/%.fir
	cd $(objdir) && $(firrtl) -i $(<F) -o $(@F) -X verilog

$(objdir)/%.fir:	$(srcdir)/%.scala
	"$(SBT)" $(SBT_FLAGS) "run $(notdir $(basename $<)) $(CHISEL_FLAGS) --targetDir $(objdir)"
