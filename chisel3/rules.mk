tut_firs    := $(addsuffix .fir, $(executables))

firs: $(tut_firs)

firrtl	?= firrtl

.PHONY:	firs

$(objdir)/%.out: $(outputdir) $(srcdir)/%.scala
	"$(SBT)" $(SBT_FLAGS) "run $(call scalasrcclass,$@) --test --targetDir $(objdir)" | tee $@

$(objdir)/%.v:	$(objdir)/%.fir
	cd $(objdir) && $(firrtl) -i $(<F) -o $(@F) -X verilog

$(objdir)/%.fir:	$(outputdir) $(srcdir)/%.scala
	"$(SBT)" $(SBT_FLAGS) "run $(call scalasrcclass,$@) $(CHISEL_FLAGS) --targetDir $(objdir)"
