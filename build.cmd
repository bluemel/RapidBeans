@echo on
call setupenv.cmd

echo #######################################################################
echo ### building rapidbeans-sdk... ########################################
echo #######################################################################
pushd rapidbeans-sdk
call mvn clean install
popd

echo #######################################################################
echo ### building rapidbeans-runtime... ####################################
echo #######################################################################
pushd rapidbeans-runtime
call mvn clean install
popd
